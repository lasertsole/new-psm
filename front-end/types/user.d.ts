export type UserInfo = {
    id?: string | undefined;
    name?: string;
    password?: string;
    repassword?: string;
    email?: string;
    avatar?: string;
    profile?: string;
    isAdmin?: boolean;
    isLogin?: boolean;
    sex?: number;//0:未知 1:男 2:女
    createTime?: string;
};