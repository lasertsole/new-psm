export type Page = {
    currentPage?: number | undefined;
    pageSize?: number | undefined;
};

export type TagBarItem = {
    tabName:string,
    index:number,
    paddingLeft?:String,
    paddingRight?:String,
};