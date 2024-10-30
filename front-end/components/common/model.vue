<template>
  <div class="demo" ref="demo"></div>
</template>

<script lang="ts" setup>
  import * as THREE from 'three';
  import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls';
  import { OBJLoader } from "three/examples/jsm/loaders/OBJLoader";
  
  const props = defineProps({
    entity: {type: String, required: false}
  })
  
  const demo = ref<HTMLElement>();

  onMounted(()=>{
    // 初始化场景
    const scene = new THREE.Scene();

      
    // 初始化相机
    const camera = new THREE.PerspectiveCamera(
      75,//视锥体角度
      demo.value!.offsetWidth / demo.value!.offsetHeight,//成像宽高比
        0.1,//近端极限
        1000//远端极限
    );

      // 设置相机初始位置
      camera.position.set(0,0,2);

      // 更新投影矩阵(窗口大小改变时动态更新)
      camera.updateProjectionMatrix();

      // 初始化渲染器
      const renderer = new THREE.WebGLRenderer({
        antialias:true // 开启抗锯齿
      });
      renderer.setSize(demo.value!.offsetWidth, demo.value!.offsetHeight);
      demo.value?.appendChild(renderer.domElement);

      // 初始化控制器
      const controls = new OrbitControls(camera, renderer.domElement);
      controls.enableDamping = true; // 开启阻尼
      
      //URL.createObjectURL
      
      // 初始化loader
      const objLoader = new OBJLoader();
      fetch('https://new-psm.oss-cn-guangzhou.aliyuncs.com/338125235918344192/models/entities/2024-10-30-18-34-40-86cf403207ca4ed49a8094b1a035a021.obj')
      .then(response => {
        console.log(response)
        return response.blob();
      }).then(blob => {
        // 将 Blob 转换为 URL
        const url = URL.createObjectURL(blob);

        objLoader.load(
          url.split(':'),
          (obj:any)=>{
            obj.scale.set(0.01,0.01,0.01);
            obj.position.set(0,0,0);
            scene.add(obj);
            
            // 释放 URL
            URL.revokeObjectURL(url);
          },
        );
      }).catch(error => {
        ElMessage.error('Failed to fetch the OBJ file', error);
      });
      
      // 添加平行光
      const light = new THREE.DirectionalLight(0xffffff, 1);
      light.position.set(0, 20, 0);
      scene.add(light);

      // 增加环境灯光
      const pointLight = new THREE.PointLight(0xffffff, 1);//增加环境光,0.1强度（无光源，四面八方都是
      scene.add(pointLight);
      
      // 渲染函数
      function render() {
        requestAnimationFrame(render);
        renderer.render(scene, camera);
        controls.update();
      }
      
      // 开始渲染
      render();

      // 监听画面变化，更新渲染画面
      window.addEventListener("resize", ()=>{
        // 更新摄像头
        camera.aspect = demo.value!.offsetWidth / demo.value!.offsetHeight;
        // 更新摄像机的投影矩阵(保证图像比例正确)
        camera.updateProjectionMatrix();
        // 更新渲染器
        renderer.setSize(demo.value!.offsetWidth, demo.value!.offsetHeight);
        // 设置渲染器的像素比
        renderer.setPixelRatio(window.devicePixelRatio);
      });
    });
</script>

<style lang="scss" scoped>
  @use "sass:math";
  @import "@/common.scss";
  
  .demo{
    @include fullInParent;
  }
</style>