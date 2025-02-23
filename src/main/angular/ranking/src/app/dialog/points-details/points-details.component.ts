import {Component, Inject, OnInit} from '@angular/core';
import {AtheletePoints} from "../../domain/Athlete";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

interface PointDetails {
  stroke: string;
  m50: number;
  m25: number;
}

@Component({
  selector: 'app-points-details',
  templateUrl: './points-details.component.html',
  styleUrls: ['./points-details.component.css']
})
export class PointsDetailsComponent implements OnInit {
  details: Array<PointDetails> = [];

  constructor(
    public dialogRef: MatDialogRef<PointsDetailsComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Array<AtheletePoints>) {
    data.forEach(ap => {
      const id = this.details.findIndex(d => d.stroke === ap.stroke.name);
      if (id == -1) {
        this.details.push({
          stroke: ap.stroke.name,
          m25: ap.course == '25m' ? ap.points : 0,
          m50: ap.course == '50m' ? ap.points : 0
        })
      } else {
        if (ap.course == '25m') {
          this.details[id].m25 = ap.points;
        } else {
          this.details[id].m50 = ap.points;
        }
      }
    })
  }

  ngOnInit(): void {
  }

}
