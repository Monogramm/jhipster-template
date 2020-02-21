export const enum ParameterType {
  URL = 'URL',
  PATH = 'PATH',
  COLOR = 'COLOR',
  DATE_TIME = 'DATE_TIME',
  TIME = 'TIME',
  DATE = 'DATE',
  DOUBLE = 'DOUBLE',
  INTEGER = 'INTEGER',
  BOOLEAN = 'BOOLEAN',
  STRING = 'STRING',
  PASSWORD = 'PASSWORD',
  ANY = 'ANY'
}

export interface IParameter {
  id?: number;
  name?: string;
  description?: string;
  type?: ParameterType;
  value?: string;
}

export class Parameter implements IParameter {
  constructor(public id?: number, public name?: string, public description?: string, public type?: ParameterType, public value?: string) {}
}
