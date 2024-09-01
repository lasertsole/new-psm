export async function login(name:string | undefined, password:string | undefined):Promise<Boolean>{
    const res:any = await getFetchData({
        url: '/users/login',
        opts: {
            name,
            password,
        },
        method: 'post',
        contentType: 'application/json',
    });

    if(res.code!=200){
        ElMessage.error('登录失败:'+res.msg);
        if(process.client)localStorage.removeItem('token');//如果在客户端运行则删除localstorage中的token
        localStorage.setItem('online', 'false');
        return false;
    }

    ElMessage.success('登录成功');

    if(process.client)
    {
        localStorage.setItem('token', res.data.token);
        localStorage.setItem('online', 'true');
    }

    return true;
}

export async function thirdPartyLogin(path:string):Promise<void>{
    window.location.href = useRuntimeConfig().public.baseURL + useRuntimeConfig().public.oauth2AuthURL + "/gitee";
}

export async function register(name:string, password:string, email:string):Promise<Boolean>{
    const res:any = await getFetchData({
        url: '/users/register',
        opts: {
            name,
            password,
            email
        },
        method: 'post',
        contentType: 'application/json',
    });

    if(res.code!=200){
        ElMessage.error('注册失败:'+res.msg);
        if(process.client)localStorage.removeItem('token');//如果在客户端运行则删除localstorage中的token
        localStorage.setItem('online', 'false');

        return false;
    }

    ElMessage.success('注册成功');
    if(process.client)
    {
        localStorage.setItem('token', res.data.token);
        localStorage.setItem('online', 'true');
    }

    return true;
}

export async function logout():Promise<Boolean>{
    const res:any = await getFetchData({
        url: '/users/register',
        method: 'post',
    });
    if(res.code==200){
        ElMessage.success('登出成功');
        if(process.client)
        {
            localStorage.removeItem('token');
            localStorage.setItem('online', 'false');
        }

        return true;
    }
    else{
        ElMessage.error('登出失败:'+res.msg);

        return false;
    }
}