import type { modelInfo } from "@/types/model";

export async function uploadModel(file:File, progressRef:Ref, targetFilePathRef:Ref):Promise<void> {
    return await tusUploadApi({
        file:file, 
        url:'/models/upload', 
        progressCB:(progress:string)=>{
            progressRef.value = progress;
        },
        successCB:()=>{
            targetFilePathRef.value = file.name;
        }
    });
};

export async function uploadModelInfo({title, content, cover, category}:modelInfo):Promise<Boolean> {
    if(!title || !content || !cover || !category) return false;
    const formData = new FormData();
    formData.append('title', title);
    formData.append('avatar', content);
    formData.append('cover', cover);
    formData.append('category', category);
    
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
    
    return true;
};