import { Calendar, momentLocalizer  } from 'react-big-calendar' 
import 'react-big-calendar/lib/css/react-big-calendar.css';
import moment from 'moment'
const localizer = momentLocalizer(moment)


const VacationCalendar = ({ vacations, users }) => {

    const events = vacations.map((vacation) => {
        const start = new Date(vacation.day);
        const end = new Date(vacation.day);
        start.setHours(8);
        end.setHours(17);
        console.log(users);
        const user = users.find(({id}) => id === vacation.vacationTaker);
        return ({
            title: `${vacation.name} (${user.firstName} ${user.lastName} - ${user.position})`,
            allDay: false,
            start,
            end,
    })});

    console.log(events)


    return (
    <div>
        <Calendar
        localizer={localizer}
        startAccessor="start"
        endAccessor="end"
        events={events}
        step={60}
        view='week'
        views={['week']}
        />
    </div>
)}

export default VacationCalendar;
