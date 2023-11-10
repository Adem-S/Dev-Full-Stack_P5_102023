import { HttpClientModule } from "@angular/common/http";
import { ComponentFixture, TestBed } from "@angular/core/testing";
import { ReactiveFormsModule } from "@angular/forms";
import { MatCardModule } from "@angular/material/card";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { MatSelectModule } from "@angular/material/select";
import { MatSnackBar, MatSnackBarModule } from "@angular/material/snack-bar";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { RouterTestingModule } from "@angular/router/testing";
import { expect } from "@jest/globals";
import { SessionService } from "src/app/services/session.service";
import { SessionApiService } from "../../services/session-api.service";

import { FormComponent } from "./form.component";
import { of } from "rxjs";
import { TeacherService } from "src/app/services/teacher.service";
import { ActivatedRoute, Router, convertToParamMap } from "@angular/router";
import { ListComponent } from "../list/list.component";

describe("FormComponent", () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;

  const sessionData = {
    name: "Session 1",
    date: "2023-11-15",
    teacher_id: "1",
    description: "Description",
  };

  const session = {
    ...sessionData,
    id: 1,
    users: [2, 3, 4],
    createdAt: new Date("2023-11-15"),
    updatedAt: new Date("2023-11-15"),
  };

  const mockSessionApiService = {
    create: jest.fn(() => of({})),
    update: jest.fn(() => of({})),
    detail: jest.fn(() => of(session)),
  };

  const mockTeacherService = {
    all: () => of([]),
  };

  const mockSessionService = {
    sessionInformation: {
      admin: true,
    },
  };

  const mockMatSnackBar = {
    open: jest.fn(),
  };

  const mockActivatedRoute = {
    snapshot: { paramMap: { get: () => "1" } },
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([
          { path: "sessions", component: ListComponent },
        ]),

        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule,
      ],
      providers: [
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: SessionService, useValue: mockSessionService },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
      ],
      declarations: [FormComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  describe("On create mode", () => {
    it("should create", () => {
      expect(component).toBeTruthy();
    });

    it("should navigate away if the user is not an admin", () => {
      const router = TestBed.inject(Router);
      const navigateSpy = jest.spyOn(router, "navigate");

      mockSessionService.sessionInformation.admin = false;

      component.ngOnInit();

      expect(navigateSpy).toHaveBeenCalledWith(["/sessions"]);
    });

    it("should make the form valid when all inputs are correctly filled", () => {
      const submitButton = fixture.nativeElement.querySelector(
        "button[type='submit']"
      );
      const sessionForm = component.sessionForm;
      expect(sessionForm).toBeDefined();

      sessionForm?.patchValue(sessionData);

      fixture.detectChanges();

      expect(submitButton.disabled).toBe(false);
      expect(sessionForm?.valid).toBe(true);
    });

    it("should make the form invalid when some inputs are not correctly filled", () => {
      const submitButton = fixture.nativeElement.querySelector(
        "button[type='submit']"
      );
      const sessionForm = component.sessionForm;
      expect(sessionForm).toBeDefined();

      sessionForm?.patchValue({
        name: "",
        date: "",
        teacher_id: "",
        description: "",
      });

      fixture.detectChanges();

      expect(sessionForm?.valid).toBe(false);
      expect(submitButton.disabled).toBe(true);
    });

    it("should create a session when submitting the form with valid data", () => {
      const router = TestBed.inject(Router);
      const navigateSpy = jest.spyOn(router, "navigate");

      const sessionForm = component.sessionForm;
      expect(sessionForm).toBeDefined();

      sessionForm?.patchValue(sessionData);

      fixture.detectChanges();

      const submitButton = fixture.nativeElement.querySelector(
        "button[type='submit']"
      );

      submitButton.click();

      expect(mockSessionApiService.create).toHaveBeenCalledWith(sessionData);

      expect(mockMatSnackBar.open).toHaveBeenCalledWith(
        "Session created !",
        "Close",
        {
          duration: 3000,
        }
      );
      expect(navigateSpy).toHaveBeenCalledWith(["sessions"]);
    });
  });

  describe("On edit mode", () => {
    beforeEach(() => {
      const router = TestBed.inject(Router);
      const fakeUrl = "/sessions/update/1";
      Object.defineProperty(router, "url", { get: () => fakeUrl });
      component.ngOnInit();
    });
    it("should create", () => {
      expect(component).toBeTruthy();
    });

    it("should create and initialize form with session data", () => {
      const sessionForm = component.sessionForm;
      expect(sessionForm).toBeDefined();
      expect(sessionForm?.value).toEqual(sessionData);
    });

    it("should update a session when submitting the form with valid data", () => {
      const router = TestBed.inject(Router);
      const navigateSpy = jest.spyOn(router, "navigate");

      const sessionForm = component.sessionForm;
      expect(sessionForm).toBeDefined();

      sessionForm?.patchValue({
        ...sessionData,
        name: "Updated Session 1",
      });

      fixture.detectChanges();

      const submitButton = fixture.nativeElement.querySelector(
        "button[type='submit']"
      );
      submitButton.click();

      expect(mockSessionApiService.update).toHaveBeenCalledWith("1", {
        ...sessionData,
        name: "Updated Session 1",
      });

      expect(mockMatSnackBar.open).toHaveBeenCalledWith(
        "Session updated !",
        "Close",
        {
          duration: 3000,
        }
      );
      expect(navigateSpy).toHaveBeenCalledWith(["sessions"]);
    });
  });
});
