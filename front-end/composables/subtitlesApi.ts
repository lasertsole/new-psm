import type { Page } from "@/types/common";
import type { Response } from "@/types/request";
import type { SubtitlesBox } from "@/types/subtitles";
export async function getSubtitlesList(page:Page<SubtitlesBox>):Promise<SubtitlesBox[] | null>{
    let currentPage:Number | undefined = page.current;
    let pageSize:Number | undefined = page.size;

    if(currentPage == undefined){
        currentPage = 1;
    }

    if(pageSize == undefined){
        pageSize = 10;
    }

    const res:Response = await fetchApi({
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