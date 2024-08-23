import type { Page } from "@/types/common";
import type { SubtitlesBox } from "@/types/subtitles";
export async function getSubtitlesList(page:Page):Promise<SubtitlesBox[] | null>{
    let currentPage:Number | undefined = page.currentPage;
    let pageSize:Number | undefined = page.pageSize;

    if(currentPage == undefined){
        currentPage = 1;
    }

    if(pageSize == undefined){
        pageSize = 10;
    }

    const res:any = await getFetchData({
        url: '/subtitles',
        method: 'get',
        opts:{
            currentPage,
            pageSize
        },
        contentType: 'application/json',
    });

    if(res.code==200){
        return res.data;
    }
    else{
        ElMessage.error(res.msg);
        return null;
    }
}