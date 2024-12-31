<template>
    <div class="appStore">
    </div>
</template>

<script setup lang="ts">
    import { pipeline } from '@huggingface/transformers';

    if(import.meta.client) {
        const transcriber = await pipeline('automatic-speech-recognition', 'Xenova/whisper-small.en');

        // Transcribe an audio file, loaded from a URL.
        const result = await transcriber('https://huggingface.co/datasets/Narsil/asr_dummy/resolve/main/mlk.flac');
        console.log(result);
    };

    definePageMeta({
        name: 'appStore'
    });
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @use "@/common.scss" as common;

    .appStore{
        @include common.fullInParent;
    }
</style>