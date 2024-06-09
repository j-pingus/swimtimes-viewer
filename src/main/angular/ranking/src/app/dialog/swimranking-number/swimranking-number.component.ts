import {Component, Inject} from "@angular/core";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";


@Component({
  selector: 'app-swimranking-number',
  templateUrl: './swimranking-number.component.html',
  styleUrls: ['./swimranking-number.component.css']
})
export class SwimrankingNumberComponent {
  constructor(
    public dialogRef: MatDialogRef<SwimrankingNumberComponent>,
    @Inject(MAT_DIALOG_DATA) public data: SwimRankingData,
  ) {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

}

export interface SwimRankingData {
  id: string;
}
