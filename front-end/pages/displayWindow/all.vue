<template>
    <div class="baseNode">
        <div class="itemBoxContainer">
            <template v-for="item in infoArr">
                <!-- <itemBox
                    :boxInfo="item"
                >
                </itemBox> -->
            </template>
        </div>
    </div>
</template>

<script setup lang="ts">
    // getSubtitlesList({currentPage:1, pageSize:10});
</script>

<style lang="scss" scoped>
    .baseNode{
        padding: 10px;
    }
</style>