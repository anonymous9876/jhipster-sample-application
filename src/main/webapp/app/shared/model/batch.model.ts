export interface IBatch {
  id?: number;
  reference?: string;
}

export class Batch implements IBatch {
  constructor(public id?: number, public reference?: string) {}
}
