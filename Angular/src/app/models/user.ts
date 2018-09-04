export class User{
	constructor(
		public _id: string,
		public username: string,
		public password: string,
		public rol: string,
		public permiso: string,
		public salt: string,
		public name: string,
		public surname: string,
		public email: string
	) {}
}
