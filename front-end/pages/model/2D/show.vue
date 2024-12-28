<template>
    <!-- <iframe class="model2DShow" src="https://demo.openwebgal.com/"></iframe> -->
    <div class="model2DShow">
        <div class="w-100 h-100">
            <ModelShow2DModel
                :entityUrl="'https://model.oml2d.com/Senko_Normals/senko.model3.json'"
            ></ModelShow2DModel>
        </div>
        <div class="w-100 h-100">
            <ModelShow2DModel
                :entityUrl="'https://model.oml2d.com/HK416-1-normal/model.json'"
            ></ModelShow2DModel>
        </div>
    </div>
</template>

<script setup lang="ts">
</script>

<style lang="scss" scoped>
    .model2DShow {
        width: 100%;
        height: 100%;
        display: flex;
    }
</style>