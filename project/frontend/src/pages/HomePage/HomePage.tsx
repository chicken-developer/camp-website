import React, { useState } from 'react';

import ButtonGroup from '@atlaskit/button/button-group';
import Button from '@atlaskit/button/standard-button';

import Modal, { ModalTransition, ScrollBehavior } from '@atlaskit/modal-dialog';
import C_calender from "../../components/c_calender";

export default function HomePage() {
    const [scrollBehavior, setScrollBehavior] = useState<ScrollBehavior>(
        'inside',
    );
    const [isOpen, setIsOpen] = useState(false);
    const close = () => setIsOpen(false);
    const setScrollAndOpen = (newScroll: ScrollBehavior) => {
        setScrollBehavior(newScroll);
        requestAnimationFrame(() => setIsOpen(true));
    };

    return (
        <div>
            <h3> This is home page</h3>
           <C_calender/>
        </div>
    );
}