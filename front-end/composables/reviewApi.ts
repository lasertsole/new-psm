import { TargetTypeEnum } from "@/enums/review.d";
import type { Review } from "@/types/review";

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

export async function getReviews(current:number, size: number, targetType:TargetTypeEnum, targetId:string):Promise<Review[]> {
    try{
        const res = await fetchApi({
            url: '/reviews',
            opts: {
                current,
                size,
                targetType,
                targetId
            }
        });

        if(res.code!=200){
            let msg = res?.msg;
            if(msg == null || msg== undefined) msg = '';
        };

        return res.data;
    } catch (error) {
        import.meta.client&&ElMessage.error('添加评论失败: '+error);

        return [];
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