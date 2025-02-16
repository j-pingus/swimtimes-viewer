import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {ReferencesService} from "../../services/references.service";
import {Observable} from "rxjs";
import {Stroke} from "../../domain/Stroke";
import {AthleteService} from "../../services/athlete.service";
import {Time} from "../../domain/Time";
import {Athlete} from "../../domain/Athlete";
import {MatDialog} from "@angular/material/dialog";
import {TimeDetailsComponent} from "../../dialog/time-details/time-details.component";
import {CompareService} from "../../services/compare.service";

@Component({
  selector: 'app-athlete',
  templateUrl: './athlete.component.html',
  styleUrls: ['./athlete.component.css']
})
export class AthleteComponent implements OnInit {
  athlete: Athlete = {
    name: '',
    id: 0,
    swimRankingId: ''
  };
  strokes: Array<StrokeTimes> = [];
  compared: Array<Athlete> = [];

  constructor(route: ActivatedRoute,
              private referenceService: ReferencesService,
              private athleteService: AthleteService,
              private compareService: CompareService,
              private router: Router,
              public dialog: MatDialog) {
    route.params.subscribe(params => {
      // (+) converts string 'id' to a number
      var athleteId = +params['id'];
      athleteService.get(athleteId).subscribe(a => {
        this.athlete = a;
      });
      this.reload(athleteId);
    });
    this.compareService.compared$.subscribe(
      c => this.compared = c
    );
  }

  reload(athleteId: number) {
    this.referenceService.getStrokes().subscribe(strokes => {
      this.strokes = strokes.map(
        stroke => {
          return {
            stroke: stroke,
            times: this.athleteService.getTimes(athleteId, stroke.id)
          }
        }
      );
    });
  }

  ngOnInit(): void {
  }

  showYear(year: string) {
    this.strokes = this.strokes.map(s => {
      return {
        stroke: s.stroke,
        times: this.athleteService.getTimesYear(this.athlete.id, s.stroke.id, year)
      }
    })
  }

  import() {
    this.athleteService.importAthelete(this.athlete.swimRankingId).subscribe(() => {
      this.reload(this.athlete.id);
    })
  }

  openDialog(time: Time) {
    this.dialog.open(TimeDetailsComponent, {
      data: time
    });
  }

  compare() {
    this.compareService.addAthlete(this.athlete);
  }

  remove(c: Athlete) {
    this.compareService.removeAthlete(c.id);
  }

  seeComparison() {
    this.router.navigate(['compared']);
  }
}

export interface StrokeTimes {
  stroke: Stroke;
  times: Observable<Array<Time>>;
}
