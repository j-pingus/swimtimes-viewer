import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Time} from "../../domain/Time";

@Component({
  selector: 'app-time-details',
  templateUrl: './time-details.component.html',
  styleUrls: ['./time-details.component.css']
})
export class TimeDetailsComponent implements OnInit {

  constructor(
    public dialogRef: MatDialogRef<TimeDetailsComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Time) {
  }

  ngOnInit(): void {
  }

}
