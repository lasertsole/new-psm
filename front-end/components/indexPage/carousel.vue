<template>
	<div class="swiperBox">
		<swiper class="Swiper"
			:slidesPerView="1"
			:spaceBetween="30"
			:loop="true"
			:centeredSlides="true"
			:pagination="{
				clickable: false,
                type: 'bullets',
			}"
			:autoplay="{
				delay: 2500,
				disableOnInteraction: true,
			}"
			:navigation="false"
            effect="coverflow"
			:modules="modules"
		>
            <template v-for="item in carouselProcessArr">
                <swiper-slide>
                     <div class="image" :style="`background-image: url(${item});`"></div>
                </swiper-slide>
            </template>
		</swiper>
	</div>
</template>

<script setup lang="ts">
    import { Swiper, SwiperSlide } from 'swiper/vue'; // swiper所需组件
    // 这是分页器和对应方法，swiper好像在6的时候就已经分离了分页器和一些其他工具
    import { Autoplay, Navigation, Pagination, A11y, EffectCoverflow } from 'swiper';
    // 引入swiper样式，对应css 如果使用less或者css只需要把scss改为对应的即可
    import 'swiper/css';
    import 'swiper/css/navigation';
    import 'swiper/css/pagination';

    const props = defineProps<{
        carouselProcessArr: string[];
    }>();

    const modules = [Autoplay, Pagination, Navigation, A11y, EffectCoverflow ];
</script>

<style lang="scss" scoped>
    @use "sass:math";
    @import "@/common.scss";
    .swiperBox{
        @include fullInParent;
        .Swiper{
            @include fullInParent;
            .image{
                @include fullInParent;
                object-fit: cover;
                background-position: center;
                background-size: 100% 100%;
                background-repeat: no-repeat;
                background-clip: border-box;
            }
        }

        :deep(.swiper-pagination){
            display: flex;
            justify-content: center;
            align-items: center;
            .swiper-pagination-bullet-active{
                width: 16px;
                height: 16px;
                background-image: url(icons/eatBean.svg);
                background-size: 100% 100%;
                background-position: center;
                background-repeat: no-repeat;
                background-color: transparent;
            }
        }
    }
</style>