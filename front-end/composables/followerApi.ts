import type { UserInfo } from "@/types/user";

export async function followUser(id:string):Promise<boolean> {
    try{
        if(userInfo.id==id) {
            import.meta.client&&ElMessage.warning('不能关注自己');
            return false;
        }
    
        const res:any = await fetchApi({
            url: `/followers/${id}`,
            method: 'post',
        });
        
        if(res.code!=200){
            let msg = res?.msg;
            if(msg == null || msg == undefined) msg = '';
            import.meta.client&&ElMessage.error('关注失败:'+ msg);
    
            return false;
        };
    
        import.meta.client&&ElMessage.success('关注成功');
    
        return true;
    }
    catch (error) {
        import.meta.client&&ElMessage.error('关注失败');

        return false;
    }
};

export async function unFollowUser(id:string):Promise<boolean> {
    try{
        if(userInfo.id==id) {
            import.meta.client&&ElMessage.warning('不能取消关注自己');
            return false;
        }
    
        const res:any = await fetchApi({
            url: `/followers/${id}`,
            method: 'delete',
        });
        
        if(res.code!=200){
            let msg = res?.msg;
            if(msg == null || msg == undefined) msg = '';
            import.meta.client&&ElMessage.error('取消关注失败:'+ msg);
    
            return false;
        };
    
        ElMessage.success('取消关注成功');
    
        return true;
    }
    catch(error){
        return false;
    }
};

export async function checkFollowing(tgtUserId:string):Promise<boolean|undefined> {
    const res:any = await fetchApi({
        url: `/followers/${tgtUserId}/self`,
        method: 'get',
    });

    if(res.code!=200){
        let msg = res?.msg;
        if(msg == null || msg == undefined) msg = '';
        import.meta.client&&ElMessage.error('获取关注信息失败:'+ msg);
    };

    import.meta.client&&ElMessage.success('获取关注信息成功');

    return res.data;
};