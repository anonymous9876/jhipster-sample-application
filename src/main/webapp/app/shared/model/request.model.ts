export interface IRequest {
  id?: number;
  reference?: string;
}

export class Request implements IRequest {
  constructor(public id?: number, public reference?: string) {}
}
