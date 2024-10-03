export async function uploadModel(file:File, progressRef:Ref, targetFilePathRef:Ref) {
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
}

export async function uploadModelInfo() {
    const res:any = await fetchApi({
        url: '/models/uploadInfo',
        opts: {
        },
        method: 'post',
        contentType: 'application/json',
    });
    return true;
}