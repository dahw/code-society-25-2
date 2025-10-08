import './Contact.scss';
import React, {PropsWithChildren} from 'react';

export interface ContactProps extends PropsWithChildren {
  title: string;
  email: string;
  phone: string;
}

export const Contact: React.FC<ContactProps> = ({title, email, phone}: ContactProps) => {
    return (<li className="contact">
        <h3>{title}</h3>
        <p>Email: {email}</p>
        <p>Phone: {phone}</p>
      </li>);
};