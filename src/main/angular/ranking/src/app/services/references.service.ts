import { Injectable } from '@angular/core';
import {environment}from '../../environments/environment';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Club} from "../domain/Club";
import {Athlete} from "../domain/Athlete";
import {Stroke} from "../domain/Stroke";
@Injectable({
  providedIn: 'root'
})
export class ReferencesService {
  private static SERVICE_URL = environment.api;

  constructor(private http: HttpClient) { }

  public getClubs():Observable<Array<Club>>{
    return this.http.get<Array<Club>>(ReferencesService.SERVICE_URL+"club");
  }
  public getStrokes():Observable<Array<Stroke>>{
    return this.http.get<Array<Stroke>>(ReferencesService.SERVICE_URL+"stroke");
  }
  public getClubAthletes(id:number):Observable<Array<Athlete>>{
    return this.http.get<Array<Athlete>>(ReferencesService.SERVICE_URL+"club/"+id+"/athletes");
  }

}
