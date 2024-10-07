<template>
  <div ref="demo" @dblclick="fullScreenOrNot"></div>
</template>

<script lang="ts" setup>
  import * as THREE from 'three';
  import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls';
  import { OBJLoader } from "three/examples/jsm/loaders/OBJLoader";
  import gasp from "gsap";
  
  const demo = ref<HTMLElement>();

  onMounted(()=>{
    // 初始化场景
    const scene = new THREE.Scene();

      
    // 初始化相机
    const camera = new THREE.PerspectiveCamera(
      75,//视锥体角度
        window.innerWidth/window.innerHeight,//成像宽高比
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
      renderer.setSize(window.innerWidth,window.innerHeight);
      demo.value.appendChild(renderer.domElement);

      // 初始化控制器
      const controls = new OrbitControls(camera, renderer.domElement);
      controls.enableDamping = true; // 开启阻尼
      
      // 初始化loader
      const objLoader = new OBJLoader();
      objLoader.load(
        '/model/model.obj',
        (obj)=>{
          obj.scale.set(0.01,0.01,0.01);
          obj.position.set(0,0,0);
          scene.add(obj);
        },
      );
      
      // 添加平行光
      const light = new THREE.DirectionalLight(0xffffff, 1);
      light.position.set(0, 20, 0);
      scene.add(light);

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
        camera.aspect = window.innerWidth / window.innerHeight;
        // 更新摄像机的投影矩阵(保证图像比例正确)
        camera.updateProjectionMatrix();
        // 更新渲染器
        renderer.setSize(window.innerWidth, window.innerHeight);
        // 设置渲染器的像素比
        renderer.setPixelRatio(window.devicePixelRatio);
      });
    });
</script>

<style scoped>
  @use "sass:math";
  @import "@/common.scss";
  
  canvas {
    @include fullInParent;
  }
</style>