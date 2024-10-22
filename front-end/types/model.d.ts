import type { Page } from "@/types/common";

export type ModelInfo = {
    id?: string;
    userId?: string;
    title?: string;
    content?: string;
    coverUrl?: string;
    modelUrl?: string;
    cover?: Blob;
    model?: Blob;
    category?: Category;
    visible?: string;
};

export type Category = {
    style: string;
    type: string;
}

export type ModelsShowBarPage = Page & {
    
}