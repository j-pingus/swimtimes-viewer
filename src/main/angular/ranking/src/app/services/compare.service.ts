import {Injectable} from '@angular/core';
import {Athlete} from "../domain/Athlete";
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class CompareService {
  private athletes: Array<Athlete> = [];
  private itemsSubject = new BehaviorSubject<Array<Athlete>>(this.athletes);
  public compared$ = this.itemsSubject.asObservable();

  constructor() {
  }

  public addAthlete(athlete: Athlete) {
    const id = this.athletes.findIndex(a => a.id === athlete.id);
    if (id !== -1) return;
    this.athletes.push(athlete);
    if (this.athletes.length > 3) {
      this.athletes.shift();
    }
    this.itemsSubject.next(this.athletes);
  }

  public removeAthlete(athleteId: number) {
    const id = this.athletes.findIndex(searched => searched.id === athleteId);
    this.athletes.splice(id, 1);
    this.itemsSubject.next(this.athletes);
  }
}
