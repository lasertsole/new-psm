import useApiFetch from '@/composables/useApiFetch'
const { $emit } = useNuxtApp();
export function login(name:string, password:string):void{
    let {data} = useApiFetch("/user/login",{
        method: 'post',
        body: {
            name,
            password,
        },
    });

    $emit('online');//登录后全局发送登录事件
}

export function register(name:string, password:string):void{
    let {data} = useApiFetch("/user/register",{
        method: 'post',
        body: {
            name,
            password,
        },
    });
    console.log(data);
}