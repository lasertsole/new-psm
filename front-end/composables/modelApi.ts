import type { Page } from "@/types/common";
import type { ModelInfo, ModelInfos, ModelInfoDetail } from "@/types/model";

export async function uploadModel(file:File, progressFuc:Function, targetFilePathRef:Ref):Promise<void> {
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
};

export async function uploadModelInfo({title, content, cover, category, visible}:ModelInfo):Promise<Boolean> {
    if(!title || !content || !cover || !category || !visible) return false;
    const formData = new FormData();
    formData.append('title', title);
    formData.append('content', content);
    formData.append('cover', cover);
    formData.append('category', JSON.stringify(category));
    formData.append('visible', visible);
    
    const res:any = await fetchApi({
        url: '/models/uploadInfo',
        opts: formData,
        method: 'post',
        contentType: 'multipart/form-data',
    });
    
    if(res.code!=200){
        let msg = res?.msg;
        if(msg == null || msg == undefined) msg = '';
        ElMessage.error('上传模型信息失败:'+ msg);

        return false;
    }

    ElMessage.success('上传模型信息成功');
    navigateTo('/');
    
    return true;
};


export async function getModelsShowBars({current, size}:Page<ModelInfos>):Promise<Page<ModelInfos>> {
    const res:any = await fetchApi({
        url: '/models',
        method: 'get',
        opts:{
            current,
            size
        }
    });
    
    return res.data;
};

export async function getModelByModelId({ modelId }:{modelId:string}):Promise<ModelInfoDetail> {
    const res:any = await fetchApi({
        url: `/models/${modelId}`,
        method: 'get',
    });
    
    return res.data;
};