export type TheEvent = {
    id: string,
    name: string,
    place: string,
    time: Date,
    description: string,
}
export type NewTheEvent = Omit<TheEvent, "id">
