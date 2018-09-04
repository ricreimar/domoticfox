import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { routing, appRoutingProviders } from './app.routing';
import {FormsModule} from '@angular/forms';
import { HttpClientModule} from '@angular/common/http';

import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { HomeComponent } from './components/home/home.component';
import { CuadrocontrolComponent } from './components/cuadrocontrol/cuadrocontrol.component';
import { ErrorComponent } from './components/error/error.component';
import { ConfiguracionComponent } from './components/configuracion/configuracion.component';
import { UserEditComponent } from './components/user-edit/user-edit.component';

import { ChartsModule } from 'ng2-charts';
import { LineaComponent } from './components/linea/linea.component';
import { MiPerfilComponent } from './components/mi-perfil/mi-perfil.component';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    HomeComponent,
    CuadrocontrolComponent,
    ErrorComponent,
    ConfiguracionComponent,
    UserEditComponent,
    LineaComponent,
    MiPerfilComponent,
  ],
  imports: [
    BrowserModule,
    routing,
    FormsModule,
    HttpClientModule,
    ChartsModule
  ],
  providers: [appRoutingProviders],
  bootstrap: [AppComponent]
})
export class AppModule { }
