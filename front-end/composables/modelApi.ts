export async function uploadModel(file:File) {
    return await tusUploadApi(file, '/models');
}