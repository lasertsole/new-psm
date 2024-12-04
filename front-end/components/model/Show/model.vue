<template>
  <div class="entity">
    <canvas class="entity" ref="entity"></canvas>
  </div>
</template>

<script lang="ts" setup>
  import * as THREE from 'three';
  import { OrbitControls } from 'three/addons/controls/OrbitControls.js';//控制器
  import { OBJLoader } from "three/examples/jsm/loaders/OBJLoader";


  const props = defineProps({
    entity: {type: String, required: false}
  });
  
  const entity = ref<HTMLCanvasElement>();

  onMounted(async ()=>{
    const THREEGPU = await import('three/webgpu');
    // 初始化场景
    const scene = new THREEGPU.Scene();

      
    // 初始化相机
    const camera = new THREEGPU.PerspectiveCamera(
      75,//视锥体角度
      entity.value!.offsetWidth / entity.value!.offsetHeight,//成像宽高比
        0.1,//近端极限
        1000//远端极限
    );

      // 设置相机初始位置
      camera.position.set(0,0,2);

      // 更新投影矩阵(窗口大小改变时动态更新)
      camera.updateProjectionMatrix();

      // 初始化渲染器
      const renderer = new THREEGPU.WebGPURenderer({
        antialias:true, // 开启抗锯齿
        canvas:entity.value,
      });
      renderer.setSize(entity.value!.offsetWidth, entity.value!.offsetHeight);
      renderer.setClearColor(new THREE.Color(0xff0000));// 背景色
      // 初始化控制器
      const controls = new OrbitControls(camera, entity.value);
      controls.enableDamping = true; // 开启阻尼   
      
      // 初始化loader
      const objLoader = new OBJLoader();
      objLoader.load(
        props.entity,
        (obj:any)=>{
          obj.scale.set(0.01,0.01,0.01);
          obj.position.set(0,0,0);
          scene.add(obj);
        },
      );
      
      // 添加平行光
      const light = new THREEGPU.DirectionalLight(0xffffff, 1);
      light.position.set(0, 20, 0);
      scene.add(light);

      // 增加环境灯光
      const pointLight = new THREEGPU.PointLight(0xffffff, 1);//增加环境光,0.1强度（无光源，四面八方都是
      scene.add(pointLight);
      
      // 渲染函数
      function render() {
        requestAnimationFrame(render);
        renderer.renderAsync(scene, camera);
        controls.update();
      };
      
      // 开始渲染
      render();

      // 监听画面变化，更新渲染画面
      window.addEventListener("resize", ()=>{
        // 更新摄像头
        camera.aspect = entity.value!.offsetWidth / entity.value!.offsetHeight;
        // 更新摄像机的投影矩阵(保证图像比例正确)
        camera.updateProjectionMatrix();
        // 更新渲染器
        renderer.setSize(entity.value!.offsetWidth, entity.value!.offsetHeight);
        // 设置渲染器的像素比
        renderer.setPixelRatio(window.devicePixelRatio);
      });
    });
</script>

<style lang="scss" scoped>
  @use "sass:math";
  @import "@/common.scss";
  
  .entity{
    @include fullInParent;
    
    canvas{
      @include fullInParent;
    }
  }
</style>