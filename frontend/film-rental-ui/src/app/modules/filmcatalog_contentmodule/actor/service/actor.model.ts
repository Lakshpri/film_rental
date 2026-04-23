export interface Actor {
  actorId: number;
  firstName: string;
  lastName: string;
  lastUpdate: string;
}

export interface ActorRequest {
  firstName: string;
  lastName: string;
}
