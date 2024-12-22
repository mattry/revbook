import { Link } from 'react-router-dom';

const ConnectionList = ({connections, title}) => {

    return(
        <>
        <br/><br/>
        <h2 className='modal-title'>{title}</h2><br/>
        {connections.length > 0 ? (
                <p>
                    {connections.map((connection) => (
                        <Link to={`/user/${connection.userId}`} className="user-link">
                                    {connection.firstName} {connection.lastName}
                        </Link>
                    ))}
                </p>
            ) : (
                <p>Nothing to display</p>
            )}
        </>
    );
}

export default ConnectionList;