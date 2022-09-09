export type TheEvent = {
    id: string,
    name: string,
    place: string,
    time: string,
    description: string,
}
export type NewTheEvent = Omit<TheEvent, "id">