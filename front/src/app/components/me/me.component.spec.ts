import { HttpClientModule } from "@angular/common/http";
import { ComponentFixture, TestBed } from "@angular/core/testing";
import { MatCardModule } from "@angular/material/card";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { MatSnackBar, MatSnackBarModule } from "@angular/material/snack-bar";
import { SessionService } from "src/app/services/session.service";

import { expect } from "@jest/globals";

import { MeComponent } from "./me.component";
import { of } from "rxjs";
import { UserService } from "src/app/services/user.service";
import { Router } from "@angular/router";

describe("MeComponent", () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  const mockUser = {
    firstName: "John",
    lastName: "Doe",
    email: "  test@test.com",
    admin: true,
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  const mockUserService = {
    getById: jest.fn((id) => of(mockUser)),
    delete: jest.fn(() => of({})),
  };

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1,
    },
    logOut: jest.fn(() => of({})),
  };

  const mockMatSnackBar = {
    open: jest.fn(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: UserService, useValue: mockUserService },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });

  it("should display user information", () => {
    expect(mockUserService.getById).toHaveBeenCalledWith(
      mockSessionService.sessionInformation.id.toString()
    );

    expect(component.user).toBe(mockUser);

    fixture.detectChanges();

    expect(fixture.nativeElement.textContent).toContain(mockUser.firstName);
    expect(fixture.nativeElement.textContent).toContain(
      mockUser.lastName.toUpperCase()
    );
    expect(fixture.nativeElement.textContent).toContain(mockUser.email);
  });

  describe("When I'm admin", () => {
    it("should display admin user information", () => {
      expect(fixture.nativeElement.textContent).toContain("You are admin");
      expect(
        fixture.nativeElement.querySelector("button[mat-raised-button]")
      ).not.toBeTruthy();
    });
  });

  describe("When I'm not admin", () => {
    beforeEach(() => {
      mockUser.admin = false;
      mockSessionService.sessionInformation.admin = false;

      component.ngOnInit();
      fixture.detectChanges();
    });
    it("should display non-admin user information", () => {
      expect(fixture.nativeElement.textContent).not.toContain("You are admin");
      expect(
        fixture.nativeElement.querySelector("button[mat-raised-button]")
      ).toBeTruthy();
    });

    it("should delete user", () => {
      const router = TestBed.inject(Router);
      const navigateSpy = jest.spyOn(router, "navigate");

      const deleteButton = fixture.nativeElement.querySelector(
        "button[mat-raised-button]"
      );

      deleteButton.click();

      expect(mockUserService.delete).toHaveBeenCalledWith(
        mockSessionService.sessionInformation.id.toString()
      );

      expect(mockSessionService.logOut).toHaveBeenCalled();

      expect(mockMatSnackBar.open).toHaveBeenCalledWith(
        "Your account has been deleted !",
        "Close",
        {
          duration: 3000,
        }
      );
      expect(navigateSpy).toHaveBeenCalledWith(["/"]);
    });
  });
});
