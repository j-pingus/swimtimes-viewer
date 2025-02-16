import {Component, OnDestroy, OnInit} from '@angular/core';
import {CompareService} from "../../services/compare.service";
import {Stroke} from "../../domain/Stroke";
import {Observable, Subscription} from "rxjs";
import {Time} from "../../domain/Time";
import {Athlete} from "../../domain/Athlete";
import {ReferencesService} from "../../services/references.service";
import {AthleteService} from "../../services/athlete.service";

@Component({
  selector: 'app-compare',
  templateUrl: './compare.component.html',
  styleUrls: ['./compare.component.css']
})
export class CompareComponent implements OnInit, OnDestroy {
  strokes: Array<ComparedStroke> = [];
  athletes: Array<Athlete> = [];
  subscription: Subscription = new Subscription();

  constructor(compareService: CompareService,
              referenceService: ReferencesService,
              private athleteService: AthleteService) {
    this.subscription.add(
      compareService.compared$.subscribe(compared => this.athletes = compared));
    referenceService.getStrokes().subscribe(strokes => {
      this.strokes = strokes.map(
        stroke => {
          return {
            stroke,
            compared: this.getComparedStrokes(stroke)
          };
        }
      );
    })

  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  ngOnInit(): void {
  }

  private getComparedStrokes(stroke: Stroke): Array<Observable<Array<Time>>> {
    const ret: Array<Observable<Array<Time>>> = []
    this.athletes.forEach(a => {
      ret.push(this.athleteService.getTimes(a.id, stroke.id))
    })
    return ret;
  }
  course : string='50m';
  compareCourse(s: string) {
    this.course=s;
  }
}

export interface ComparedStroke {
  stroke: Stroke;
  compared: Array<Observable<Array<Time>>>;
}
