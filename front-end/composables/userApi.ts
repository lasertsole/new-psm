export async function login(name:string, password:string):Promise<Boolean>{
    const res:any = await getFetchData({
        url: '/users/login',
        opts: {
            name,
            password,
        },
        method: 'post',
    });
    if(res.code==200){
        ElMessage.success('登录成功');
        if(process.client)
        {
            localStorage.setItem('token', res.data.token);
            localStorage.setItem('online', 'true');
        }
        return true;
    }
    else{
        ElMessage.error('登录失败:'+res.msg);
        if(process.client)localStorage.removeItem('token');//如果在客户端运行则删除localstorage中的token
        localStorage.setItem('online', 'false');
        return false;
    }
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
    });
    if(res.code==200){
        ElMessage.success('注册成功');
        if(process.client)
        {
            localStorage.setItem('token', res.data.token);
            localStorage.setItem('online', 'true');
        }
        return true;
    }
    else{
        ElMessage.error('注册失败:'+res.msg);
        if(process.client)localStorage.removeItem('token');//如果在客户端运行则删除localstorage中的token
        localStorage.setItem('online', 'false');
        return false;
    }
}