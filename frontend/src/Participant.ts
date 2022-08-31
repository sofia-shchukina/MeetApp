export type Participant = {
    id: string,
    name: string,
    email: string,
}
export type NewParticipant = Omit<Participant, "id">
