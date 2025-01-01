import type { UserInfo } from "@/types/user";
import type { Response } from "@/types/response";
import type { Page, ESResult } from "@/types/common";
import type { Model3DInfo, Model3DInfos, Model3DInfoDetail } from "@/types/model3d";

/**
 * 上传模型实体
 * 
 * @param { File } file 模型文件
 * @param { Function } progressFuc 上传进度变化时的回调函数
 * @param { Ref } targetFilePathRef 文件上传到云端路径ref
 */
export async function uploadModel3d(file:File, progressFuc:Function, targetFilePathRef:Ref):Promise<void> {
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

/**
 * 上传模型信息
 * 
 * @param { string } title 模型标题
 * @param { string } content 模型内容 
 * @param { string | Blob } cover 模型封面
 * @param { string } style 模型风格
 * @param { string } type 模型类型
 * @param { string } visible 模型可见性
 * @returns { Promise<Boolean> } 上传模型信息是否成功
 */
export async function uploadModel3dInfo({title, content, cover, style, type, visible}:Model3DInfo):Promise<Boolean> {
    try{
        if(!title || !content || !cover || !style || !type || !visible) return false;
        const formData = new FormData();
        formData.append('title', title);
        formData.append('content', content);
        formData.append('coverFile', cover);
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
        };
    
        import.meta.client&&ElMessage.success('上传模型信息成功');
        navigateTo('/');
        
        return true;
    } catch (error) {
        import.meta.client&&ElMessage.error('上传模型信息失败');

        return false;
    }
};

/**
 * 获取模型信息
 * 
 * @param { number } current 当前页号
 * @param { number } size 页大小
 * @param { boolean } isIdle 是否闲置
 * @param { boolean } canUrgent 是否可加急
 * @param { string } style 模型风格
 * @param { string } type 模型可见性
 * @returns { Promise<Page<Model3DInfos>> } 模型上传信息分页
 */
export async function getModel3dsShowBars(
    {current, size, isIdle, canUrgent, style, type}:
    Page<Model3DInfos>&UserInfo&Model3DInfo): Promise<Page<Model3DInfos>> {

    try{
        let opts:Page<Model3DInfos>&UserInfo&Model3DInfo = {
            current,
            size,
            isIdle,
            canUrgent,
            style,
            type
        };
        
        const res:Response = await fetchApi({
            url: '/models',
            method: 'get',
            opts
        });

        if(res.code!=200){
            import.meta.client&&ElMessage.error('获取模型失败');

            return {
                current,
                size,
                total: 0,
                records:[],
                pages: 1
            };
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
        };
    };
};

/**
 * 获取关注用户的模型信息
 * 
 * @param { number } current 当前页号
 * @param { number } size 页大小
 * @param { boolean } isIdle 是否闲置
 * @param { boolean } canUrgent 是否可加急
 * @param { string } style 模型风格
 * @param { string } type 模型可见性
 * @returns { Promise<Page<Model3DInfos>> } 模型信息分页
 */
export async function getFollowingModel3dsShowBars(
    {current, size, isIdle, canUrgent, style, type}:
    Page<Model3DInfos>&UserInfo&Model3DInfo): Promise<Page<Model3DInfos>> {

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
            };
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
        };
    };
};

/**
 * 根据ID获取模型信息
 * 
 * @param { string } modelId 当前页号
 * @returns { Promise<Model3DInfoDetail> } 模型详细信息分页
 */
export async function getModelByModel3dId({ modelId }:{modelId:string}):Promise<Model3DInfoDetail> {
    try {
        const res:Response = await fetchApi({
            url: `/models/${modelId}`,
            method: 'get',
        });
        
        return res.data;
    } catch (error) {
        import.meta.client&&ElMessage.error('获取模型失败');
        
        return {} as Model3DInfoDetail; 
    };
};

/**
 * 根据关键字匹配模型粗略信息
 * 
 * @param { string } keyword 关键字
 * @returns { Promise<ESResult[]> } es搜索结果列表
 */
export async function blurSearchModel3d(keyword:string):Promise<ESResult[]> {
    try {
        const res:Response = await fetchApi({
            url: `/models/blurSearch`,
            method: 'get',
            opts:{keyword}
        });
    
        return res.data;
    } catch (error) {
        import.meta.client&&ElMessage.error('搜索模型失败');
        
        return []; 
    };
};

/**
 * 根据关键字匹配模型详细信息
 * 
 * @param { string } keyword 关键字
 * @param { string } afterKeyId 下一页分页id，用于加速es搜索
 * @param { number } size 分页大小
 * @returns { Promise<ESResult[]> } es搜索结果列表
 */
export async function detailSearchModel3d(keyword:string, afterKeyId: string | null, size: number):Promise<Page<ESResult>> {
    try {
        const res:Response = await fetchApi({
            url: `/models/detailSearch`,
            method: 'get',
            opts:{
                keyword,
                afterKeyId,
                size
            }
        });
    
        return res.data;
    } catch (error) {
        import.meta.client&&ElMessage.error('搜索模型失败');
        
        return {
            total: 0,
            records: [] as ESResult[],
            size: 10,
            nextAfterKeys: []
        }; 
    };
};