import { HttpClientModule } from "@angular/common/http";
import { ComponentFixture, TestBed } from "@angular/core/testing";
import { ReactiveFormsModule } from "@angular/forms";
import { MatSnackBarModule } from "@angular/material/snack-bar";
import { RouterTestingModule } from "@angular/router/testing";
import { expect } from "@jest/globals";
import { SessionService } from "../../../../services/session.service";
import { MatSnackBar } from "@angular/material/snack-bar";

import { DetailComponent } from "./detail.component";
import { of } from "rxjs";
import { ActivatedRoute, Router } from "@angular/router";
import { SessionApiService } from "../../services/session-api.service";
import { TeacherService } from "src/app/services/teacher.service";
import { MatCardModule } from "@angular/material/card";
import { MatIconModule } from "@angular/material/icon";
import { ListComponent } from "../list/list.component";

describe("DetailComponent", () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;

  const mockActivatedRoute = {
    snapshot: { paramMap: { get: () => "1" } },
  };

  const mockSessionService = {
    sessionInformation: { admin: true, id: 1 },
  };

  const mockSessionServiceNotAdmin = {
    sessionInformation: {
      admin: false,
      id: 1,
    },
  };

  const mockTeacher = {
    id: "1",
    firstName: "John",
    lastName: "Doe",
  };

  const mockSession = {
    id: "1",
    name: "Session 1",
    description: "Session 1",
    date: new Date("2023-11-15"),
    teacher_id: 1,
    users: [2, 3, 4],
    createdAt: new Date("2023-11-15"),
    updatedAt: new Date("2023-11-15"),
  };

  const mockSessionApiService = {
    detail: jest.fn(() => of(mockSession)),
    participate: jest.fn(() => of({})),
    unParticipate: () => of({}),
    delete: jest.fn(() => of({})),
  };

  const mockTeacherService = {
    detail: jest.fn(() => of(mockTeacher)),
  };

  const mockMatSnackBar = {
    open: jest.fn(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([
          { path: "sessions", component: ListComponent },
        ]),
        HttpClientModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        ReactiveFormsModule,
      ],
      declarations: [DetailComponent],
      providers: [
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
      ],
    }).compileComponents();
  });

  describe("When user is admin", () => {
    beforeEach(() => {
      TestBed.overrideProvider(SessionService, {
        useValue: mockSessionService,
      });
      fixture = TestBed.createComponent(DetailComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    it("should create", () => {
      expect(component).toBeTruthy();
    });

    it("should fetch session data", () => {
      expect(mockSessionApiService.detail).toHaveBeenCalledWith(mockSession.id);
      expect(mockTeacherService.detail).toHaveBeenCalledWith(mockTeacher.id);

      expect(component.session).toEqual(mockSession);
      expect(component.isParticipate).toBeFalsy();
      expect(component.teacher).toEqual(mockTeacher);
      expect(component.isAdmin).toBeTruthy();
      expect(component.userId).toBe(
        mockSessionService.sessionInformation.id.toString()
      );
    });

    it("should display session details", () => {
      expect(
        fixture.nativeElement.querySelector(".mat-card-title h1").textContent
      ).toBe(mockSession.name);
      expect(
        fixture.nativeElement.querySelector(".description").textContent
      ).toContain(mockSession.description);
    });

    it("should display teacher details", () => {
      expect(
        fixture.nativeElement.querySelector(".mat-card-subtitle .ml1")
          .textContent
      ).toContain(
        `${mockTeacher.firstName} ${mockTeacher.lastName.toUpperCase()}`
      );
    });

    it("should display 'delete' button", () => {
      const allButtons = fixture.nativeElement.querySelectorAll(
        "button[mat-raised-button]"
      );

      expect(allButtons.length).toBe(1);
      expect(allButtons[0].textContent).toContain("Delete");
    });

    it("should not display 'participate' button", () => {
      const allButtons = fixture.nativeElement.querySelectorAll(
        "button[mat-raised-button]"
      );
      expect(allButtons.length).toBe(1);
      expect(allButtons[0].textContent).not.toContain("Participate");
    });

    it("should delete session", () => {
      const router = TestBed.inject(Router);
      const navigateSpy = jest.spyOn(router, "navigate");
      component.delete();

      expect(mockSessionApiService.delete).toHaveBeenCalledWith(mockSession.id);
      expect(mockMatSnackBar.open).toHaveBeenCalledWith(
        "Session deleted !",
        "Close",
        {
          duration: 3000,
        }
      );
      expect(navigateSpy).toHaveBeenCalledWith(["sessions"]);
    });
  });

  describe("When user is not admin", () => {
    beforeEach(() => {
      TestBed.overrideProvider(SessionService, {
        useValue: mockSessionServiceNotAdmin,
      });
      fixture = TestBed.createComponent(DetailComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    it("should display 'participate' button", () => {
      const allButtons = fixture.nativeElement.querySelectorAll(
        "button[mat-raised-button]"
      );
      expect(allButtons.length).toBe(1);
      expect(allButtons[0].textContent).toContain("Participate");
    });

    it("should not display 'delete' button", () => {
      const allButtons = fixture.nativeElement.querySelectorAll(
        "button[mat-raised-button]"
      );

      expect(allButtons.length).toBe(1);
      expect(allButtons[0].textContent).not.toContain("Delete");
    });

    it("should participate in the session", async () => {
      const participateButton = fixture.nativeElement.querySelector(
        "button[mat-raised-button]"
      );

      participateButton.click();
      expect(mockSessionApiService.participate).toHaveBeenCalledWith(
        mockSession.id,
        mockSessionServiceNotAdmin.sessionInformation.id.toString()
      );
    });
  });
});
