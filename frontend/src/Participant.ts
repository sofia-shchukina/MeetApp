export type Participant = {
    id: string,
    name: string,
}
export type NewParticipant = Omit<Participant, "id">
