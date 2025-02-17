import {Stroke} from "./Stroke";

export interface Athlete{
  id:number;
  name:string;
  swimRankingId:string;
}
export interface AtheletePoints{
  points:number;
  stroke:Stroke;
  course:string;
}
