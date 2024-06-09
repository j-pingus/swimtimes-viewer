import {Component, OnInit} from '@angular/core';
import {ReferencesService} from "../services/references.service";
import {Observable} from "rxjs";
import {Club} from "../domain/Club";
import {Router} from "@angular/router";
import {SwimrankingNumberComponent} from "../dialog/swimranking-number/swimranking-number.component";
import {MatDialog} from "@angular/material/dialog";
import {AthleteService} from "../services/athlete.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  clubs: Observable<Array<Club>>;

  constructor(private referenceService: ReferencesService,
              private athleteService: AthleteService,
              private router: Router,
              public dialog: MatDialog) {
    this.clubs = this.referenceService.getClubs();
  }

  go(club: number) {
    this.router.navigate(["/club", club]);
  }

  ngOnInit(): void {
  }

  import() {
    const dialogRef = this.dialog.open(SwimrankingNumberComponent, {
      data: {},
    });

    dialogRef.afterClosed().subscribe(id => {
      this.athleteService.importAthelete(id).subscribe(
        () => this.clubs = this.referenceService.getClubs()
      );
    });
  }
}
