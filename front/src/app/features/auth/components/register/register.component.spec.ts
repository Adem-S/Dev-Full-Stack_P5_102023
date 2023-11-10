import { HttpClientModule } from "@angular/common/http";
import {
  HttpClientTestingModule,
  HttpTestingController,
} from "@angular/common/http/testing";
import { ComponentFixture, TestBed } from "@angular/core/testing";
import { FormBuilder, ReactiveFormsModule } from "@angular/forms";
import { MatCardModule } from "@angular/material/card";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { expect } from "@jest/globals";
import { AuthService } from "../../services/auth.service";
import { RouterTestingModule } from "@angular/router/testing";
import { LoginComponent } from "../login/login.component";
import { Router } from "@angular/router";
import { RegisterComponent } from "./register.component";

describe("RegisterComponent", () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [AuthService],
      imports: [
        RouterTestingModule.withRoutes([
          { path: "login", component: LoginComponent },
        ]),
        HttpClientTestingModule,
        HttpClientModule,
        BrowserAnimationsModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
      ],
    }).compileComponents();

    httpMock = TestBed.inject(HttpTestingController);
    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should create and initialize 'onError' to false", () => {
    expect(component).toBeTruthy();
    expect(component.onError).toBe(false);
  });

  it("should validate the form with valid data", () => {
    const form = component.form;

    expect(form.contains("firstName")).toBeTruthy();
    expect(form.contains("lastName")).toBeTruthy();
    expect(form.contains("email")).toBeTruthy();
    expect(form.contains("password")).toBeTruthy();

    let firstName = form.get("firstName");
    let lastName = form.get("lastName");
    let email = form.get("email");
    let password = form.get("password");

    firstName?.setValue("John");
    lastName?.setValue("Doe");
    email?.setValue("test@test.com");
    password?.setValue("password");

    expect(firstName?.valid).toBeTruthy();
    expect(lastName?.valid).toBeTruthy();
    expect(email?.valid).toBeTruthy();
    expect(password?.valid).toBeTruthy();
    expect(form.valid).toBeTruthy();
  });

  it("should not validate the form with invalid data", () => {
    const form = component.form;

    expect(form.contains("firstName")).toBeTruthy();
    expect(form.contains("lastName")).toBeTruthy();
    expect(form.contains("email")).toBeTruthy();
    expect(form.contains("password")).toBeTruthy();

    let firstName = form.get("firstName");
    let lastName = form.get("lastName");
    let email = form.get("email");
    let password = form.get("password");

    firstName?.setValue("");
    lastName?.setValue("D");
    email?.setValue("test");
    password?.setValue("");

    expect(email?.valid).toBeFalsy();
    expect(password?.valid).toBeFalsy();
    expect(form.valid).toBeFalsy();
  });

  it("should register successfully", async () => {
    expect(component.onError).toBe(false);

    const form = component.form;
    const authService = TestBed.inject(AuthService);
    const router = TestBed.inject(Router);

    const authServiceSpy = jest.spyOn(authService, "register");
    const navigateSpy = jest.spyOn(router, "navigate");

    form.get("firstName")?.setValue("John");
    form.get("lastName")?.setValue("Doe");
    form.get("email")?.setValue("test@example.com");
    form.get("password")?.setValue("password123");

    component.submit();

    const req = httpMock.expectOne("api/auth/register");
    expect(req.request.method).toBe("POST");
    req.flush({ message: "User registered successfully!" });

    fixture.detectChanges();

    expect(authServiceSpy).toHaveBeenCalledWith({
      firstName: "John",
      lastName: "Doe",
      email: "test@example.com",
      password: "password123",
    });

    expect(component.onError).toBe(false);

    expect(navigateSpy).toHaveBeenCalledWith(["/login"]);
  });

  it("should not register with error", async () => {
    expect(component.onError).toBe(false);

    const form = component.form;
    const authService = TestBed.inject(AuthService);
    const router = TestBed.inject(Router);

    const authServiceSpy = jest.spyOn(authService, "register");
    const navigateSpy = jest.spyOn(router, "navigate");

    form.get("firstName")?.setValue("John");
    form.get("lastName")?.setValue("Doe");
    form.get("email")?.setValue("test@example.com");
    form.get("password")?.setValue("password123");

    component.submit();

    const req = httpMock.expectOne("api/auth/register");
    expect(req.request.method).toBe("POST");
    req.flush("error", { status: 404, statusText: "Not Found" });

    fixture.detectChanges();

    expect(authServiceSpy).toHaveBeenCalledWith({
      firstName: "John",
      lastName: "Doe",
      email: "test@example.com",
      password: "password123",
    });

    expect(component.onError).toBe(true);

    expect(navigateSpy).not.toHaveBeenCalledWith(["/login"]);
  });
});
