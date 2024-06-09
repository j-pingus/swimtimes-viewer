import {Injectable} from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Time} from "../domain/Time";
import {Athlete} from "../domain/Athlete";

@Injectable({
  providedIn: 'root'
})
export class AthleteService {

  private static SERVICE_URL = environment.api + "athlete/";

  constructor(private http: HttpClient) {
  }

  public getTimes(athlete: number, stroke: number): Observable<Array<Time>> {
    return this.http.get<Array<Time>>(AthleteService.SERVICE_URL + athlete + "/stroke/" + stroke + "/time");
  }
 public getTimesYear(athlete: number, stroke: number, year:string): Observable<Array<Time>> {
    return this.http.get<Array<Time>>(AthleteService.SERVICE_URL + athlete + "/stroke/" + stroke+ "/year/" + year + "/time");
  }

  public get(athlete: number): Observable<Athlete> {
    return this.http.get<Athlete>(AthleteService.SERVICE_URL + athlete);
  }

  public importAthelete(athlete: string): Observable<any> {
    return this.http.get(AthleteService.SERVICE_URL + athlete + "/import");
  }
}
