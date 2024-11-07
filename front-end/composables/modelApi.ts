import type { Page } from "@/types/common";
import type { UserInfo } from "@/types/user";
import type { Response } from "@/types/request";
import type { ModelInfo, ModelInfos, ModelInfoDetail } from "@/types/model";

export async function uploadModel(file:File, progressFuc:Function, targetFilePathRef:Ref):Promise<void> {
    try{
        return await tusUploadApi({
            file:file, 
            url:'/models/upload', 
            progressCB:(progress:string)=>{
                progressFuc(progress);
            },
            successCB:()=>{
                targetFilePathRef.value = file.name;
            }
        });
    }
    catch (error) {
        import.meta.client&&ElMessage.error('上传模型失败');
    }
};

export async function uploadModelInfo({title, content, cover, style, type, visible}:ModelInfo):Promise<Boolean> {
    try{
        if(!title || !content || !cover || !style || !type || !visible) return false;
        const formData = new FormData();
        formData.append('title', title);
        formData.append('content', content);
        formData.append('cover', cover);
        formData.append('style', style);
        formData.append('type', type);
        formData.append('visible', visible);
        
        const res:Response = await fetchApi({
            url: '/models/uploadInfo',
            opts: formData,
            method: 'post',
            contentType: 'multipart/form-data',
        });
        
        if(res.code!=200){
            let msg = res?.msg;
            if(msg == null || msg == undefined) msg = '';
            import.meta.client&&ElMessage.error('上传模型信息失败:'+ msg);
    
            return false;
        }
    
        import.meta.client&&ElMessage.success('上传模型信息成功');
        navigateTo('/');
        
        return true;
    }
    catch (error) {
        import.meta.client&&ElMessage.error('上传模型信息失败');

        return false;
    }
};


export async function getModelsShowBars(
    {current, size, isIdle, canUrgent, style, type}:
    Page<ModelInfos>&UserInfo&ModelInfo): Promise<Page<ModelInfos>> {

    try{
        const res:Response = await fetchApi({
            url: '/models',
            method: 'get',
            opts:{
                current,
                size,
                isIdle,
                canUrgent,
                style,
                type
            }
        });

        if(res.code!=200){
            import.meta.client&&ElMessage.error('获取模型失败');

            return {
                current,
                size,
                total: 0,
                records:[],
                pages: 1
            } as Page<ModelInfos>;
        };
        
        return res.data;

    } catch (error) {
        import.meta.client&&ElMessage.error('获取模型失败');

        return {
            current,
            size,
            total: 0,
            records:[],
            pages: 1
        } as Page<ModelInfos>;
    }
};

export async function getFollowingModelsShowBars(
    {current, size, isIdle, canUrgent, style, type}:
    Page<ModelInfos>&UserInfo&ModelInfo): Promise<Page<ModelInfos>> {

    try {
        const res:Response = await fetchApi({
            url: '/models',
            method: 'get',
            opts:{
                current,
                size,
                isIdle,
                canUrgent,
                style,
                type,
                isFollowing: "true"
            }
        });
    
        if(res.code!=200){
            import.meta.client&&ElMessage.error('获取关注模型失败');
    
            return {
                current,
                size,
                total: 0,
                records:[],
                pages: 1
            } as Page<ModelInfos>;
        };
        
        return res.data;

    } catch (error) {
        import.meta.client&&ElMessage.error('获取关注模型失败');

        return {
            current,
            size,
            total: 0,
            records:[],
            pages: 1
        } as Page<ModelInfos>;
    }
};

export async function getModelByModelId({ modelId }:{modelId:string}):Promise<ModelInfoDetail> {
    try {
        const res:Response = await fetchApi({
            url: `/models/${modelId}`,
            method: 'get',
        });
        
        return res.data;
    }
    catch (error) {
        import.meta.client&&ElMessage.error('获取模型失败');
        
        return {} as ModelInfoDetail; 
    }
};