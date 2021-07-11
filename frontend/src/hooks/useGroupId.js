const { useParams } = require('react-router-dom')

const useGroupId = () => {
    const params = useParams();
    
    return params.groupId;
}

export default useGroupId;
