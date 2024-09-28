export async function uploadModel(file:File, progressRef:Ref) {
    return await tusUploadApi({file:file, url:'/models/upload',progressCB:(progress:string)=>{
        progressRef.value = progress;
    }});
}