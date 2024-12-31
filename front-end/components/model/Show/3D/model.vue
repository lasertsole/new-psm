<template>
  <el-main class="entity" :loading="loading">
    <canvas class="entity" ref="entity"
      @dblclick="fullScreenOrNot"
      @keydown.enter="snapShotEvent"
    >
    </canvas>
  </el-main>
</template>

<script lang="ts" setup>
  import * as THREE from 'three';
  import { OrbitControls } from 'three/addons/controls/OrbitControls.js';//轨道控制器
  import { OBJLoader } from "three/examples/jsm/loaders/OBJLoader";

  const loading:Ref<Boolean> = ref<Boolean>(true);

  const props = defineProps({
    entityUrl: {type: String, required: false},
    isSnapshot: {type: Boolean, required: false}
  });
  
  const emits = defineEmits(['snapShotBlob']);

  const entity = ref<HTMLCanvasElement>();

  let renderer: any;

  function snapShotEvent(event: KeyboardEvent):void {
    if(event.key!="Enter" ||!entity.value) return;

    entity.value!.toBlob((blob:Blob|null)=>{
      if(!blob) return;

      const file = new File([blob], 'snapshot.jpg', { type: blob.type });
      emits('snapShotBlob', file);
    }, 'image/jpeg', 0.9);
    
    document.exitFullscreen();
    document.removeEventListener('keydown', snapShotEvent);
  };

  /*鼠标双击全屏或退出全屏*/
  function fullScreenOrNot():void {
    if(!document||!renderer) return;
    if(!document.fullscreenElement) { //若不存在已全屏dom,则进入全屏。分钟退出全屏
      renderer.domElement.requestFullscreen();
      if(props.isSnapshot){
        document.addEventListener('keydown', snapShotEvent);
      };
    }
    else{
      document.exitFullscreen();
      if(props.isSnapshot){
        document.removeEventListener('keydown', snapShotEvent);
      };
    }
  }

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
      renderer = new THREEGPU.WebGPURenderer({
        antialias:true, // 开启抗锯齿
        canvas:entity.value,
      });
      renderer.setSize(entity.value!.offsetWidth, entity.value!.offsetHeight);
      renderer.setClearColor(new THREE.Color(0x000000));// 背景色
      // 初始化控制器
      const controls = new OrbitControls(camera, entity.value);
      controls.enableDamping = true; // 开启阻尼   
      
      // 初始化loader
      const objLoader = new OBJLoader();
      objLoader.load(
        props.entityUrl,
        (obj:any)=>{
          obj.scale.set(0.01,0.01,0.01);
          obj.position.set(0,0,0);
          scene.add(obj);
          loading.value = false;
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
  @use "@/common.scss" as common;
  
  .entity{
    @include common.fullInParent;
    padding: 0px;

    canvas{
      @include common.fullInParent;
    }
  }
</style>