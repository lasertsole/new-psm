import type { Page } from "@/types/common";
import type { Review } from "@/types/review";
import { TargetTypeEnum } from "@/enums/review.d";

export async function addReview(review:Review):Promise<Boolean> {
    try{
        const res = await fetchApi({
            url: '/reviews',
            method: 'post',
            opts: review
        });

        if(res.code!=200){
            let msg = res?.msg;
            if(msg == null || msg == undefined) msg = '';
            import.meta.client&&ElMessage.error('添加评论失败');
    
            return false;
        };

        import.meta.client&&ElMessage.success('添加评论成功');
        return true;
    } catch (error) {
        import.meta.client&&ElMessage.error('添加评论失败:'+error);

        return false;
    };
};

export async function getReviews(
    {
        current,
        size,
        targetType,
        targetId,
        attachUserId
    }:
    {
        current:number,
        size: number,
        targetType:TargetTypeEnum,
        targetId:string,
        attachUserId?:string
    }):Promise<Page<Review>> {
    try{
        const res = await fetchApi({
            url: '/reviews',
            opts: {
                current,
                size,
                targetType,
                targetId,
                attachUserId
            }
        });

        if(res.code!=200){
            let msg = res?.msg;
            if(msg == null || msg== undefined) msg = '';
        };

        return res.data;
    } catch (error) {
        import.meta.client&&ElMessage.error('添加评论失败: '+error);

        return {
            current,
            size,
            total: 0,
            records:[],
            pages: 1,
        };
    };
};

export async function deleteReview(id:string):Promise<Boolean> {
    try{
        const res = await fetchApi({
            url: '/reviews',
            method: 'delete',
            opts: {
                id
            }
        });

        if(res.code!=200){
            let msg = res?.msg;
            if(msg == null || msg == undefined) msg = '';
            import.meta.client&&ElMessage.error('删除评论失败');
        };

        import.meta.client&&ElMessage.success('删除评论成功');

        return true;
    } catch (error) {
        import.meta.client&&ElMessage.error('删除评论失败: '+error);

        return false;
    };
};