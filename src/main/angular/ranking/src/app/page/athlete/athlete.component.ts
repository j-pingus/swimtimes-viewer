import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {ReferencesService} from "../../services/references.service";
import {Observable} from "rxjs";
import {Stroke} from "../../domain/Stroke";
import {AthleteService} from "../../services/athlete.service";
import {Time} from "../../domain/Time";
import {Athlete} from "../../domain/Athlete";
import {MatDialog} from "@angular/material/dialog";
import {TimeDetailsComponent} from "../../dialog/time-details/time-details.component";

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

  constructor(route: ActivatedRoute,
              private referenceService: ReferencesService,
              private athleteService: AthleteService,
              public dialog: MatDialog) {
    route.params.subscribe(params => {
      // (+) converts string 'id' to a number
      var athleteId = +params['id'];
      athleteService.get(athleteId).subscribe(a => {
        this.athlete = a;
      });
      this.reload(athleteId);
    });
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

  import(swimRankingId: string) {
    this.athleteService.importAthelete(this.athlete.swimRankingId).subscribe(() => {
      this.reload(this.athlete.id);
    })
  }

  openDialog(time: Time) {
    this.dialog.open(TimeDetailsComponent, {
      data: time
    });
  }
}

export interface StrokeTimes {
  stroke: Stroke;
  times: Observable<Array<Time>>;
}
