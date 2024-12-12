declare module 'three/examples/jsm/controls/OrbitControls' {
  export class OrbitControls {
      constructor(object: THREE.Camera, domElement?: HTMLElement);
      enableDamping: boolean;
      update(): void;
  };
}

declare module "three/examples/jsm/loaders/OBJLoader" {
    class OBJLoader {
        load(url: string | undefined, onLoad: (object: THREE.Object3D) => void, onProgress?: (event: ProgressEvent) => void, onError?: (event: ErrorEvent) => void): void;
    }
    export {OBJLoader};
}

declare module 'rxjs' {
  export function fromEvent<T>(target: any, eventName: string, options?: EventListenerOptions | ((...args: any[]) => T), resultSelector?: (...args: any[]) => T): Observable<T>;
  export function concatAll<O extends ObservableInput<any>>(): OperatorFunction<O, ObservedValueOf<O>>;
  export function concatMap<T, R, O extends ObservableInput<any>>(project: (value: T, index: number) => O, resultSelector?: (outerValue: T, innerValue: ObservedValueOf<O>, outerIndex: number, innerIndex: number) => R): OperatorFunction<T, ObservedValueOf<O> | R>;
  export function scan<V, A, S>(accumulator: (acc: V | A | S, value: V, index: number) => A, seed?: S): OperatorFunction<V, V | A>;
  export function filter<T>(predicate: (value: T, index: number) => boolean, thisArg?: any): MonoTypeOperatorFunction<T>;
  export function map(project: (value: T, index: number) => R): OperatorFunction<T, R>;
  export function tap(observerOrNext?: Partial<TapObserver<T>> | ((value: T) => void)): MonoTypeOperatorFunction<T>;
};

declare module 'dplayer' {
  export default class DPlayer{
    video:  HTMLVideoElement;
    constructor({
      container: HTMLElement,
      video: {
        url: string
      },
    });
  };
};