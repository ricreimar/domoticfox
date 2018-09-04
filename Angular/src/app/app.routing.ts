
import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { HomeComponent } from './components/home/home.component';
import { ConfiguracionComponent } from './components/configuracion/configuracion.component';
import { CuadrocontrolComponent } from './components/cuadrocontrol/cuadrocontrol.component';
import { ErrorComponent } from './components/error/error.component';
import { UserEditComponent } from './components/user-edit/user-edit.component';
import { LineaComponent } from './components/linea/linea.component';
import { MiPerfilComponent } from './components/mi-perfil/mi-perfil.component';


const appRoutes: Routes = [
    {path: '', component: HomeComponent},
    {path: 'home', component: HomeComponent},
    {path: 'login', component: LoginComponent},
    {path: 'registro', component: RegisterComponent},
    {path: 'cuadrocontrol', component: CuadrocontrolComponent},
    {path: 'configuracion', component: ConfiguracionComponent},
    {path: 'mis-datos', component: UserEditComponent},
    {path: 'mi-perfil', component: MiPerfilComponent},
    {path: 'graficos', component: LineaComponent},
    {path: '**', component: ErrorComponent}
];

export const appRoutingProviders: any[] = [];
export const routing: ModuleWithProviders = RouterModule.forRoot(appRoutes);
